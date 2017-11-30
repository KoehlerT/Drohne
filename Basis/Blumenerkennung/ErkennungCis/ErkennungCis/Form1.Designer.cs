namespace ErkennungCis
{
    partial class Form1
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            this.image_Box = new System.Windows.Forms.PictureBox();
            this.btn_opem = new System.Windows.Forms.Button();
            this.openFileDialog = new System.Windows.Forms.OpenFileDialog();
            ((System.ComponentModel.ISupportInitialize)(this.image_Box)).BeginInit();
            this.SuspendLayout();
            // 
            // image_Box
            // 
            this.image_Box.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.image_Box.Location = new System.Drawing.Point(13, 13);
            this.image_Box.Name = "image_Box";
            this.image_Box.Size = new System.Drawing.Size(600, 600);
            this.image_Box.TabIndex = 0;
            this.image_Box.TabStop = false;
            // 
            // btn_opem
            // 
            this.btn_opem.Location = new System.Drawing.Point(673, 13);
            this.btn_opem.Name = "btn_opem";
            this.btn_opem.Size = new System.Drawing.Size(75, 23);
            this.btn_opem.TabIndex = 1;
            this.btn_opem.Text = "öffnen";
            this.btn_opem.UseVisualStyleBackColor = true;
            this.btn_opem.Click += new System.EventHandler(this.btn_opem_Click);
            // 
            // openFileDialog
            // 
            this.openFileDialog.FileName = "openFileDialog";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(760, 618);
            this.Controls.Add(this.btn_opem);
            this.Controls.Add(this.image_Box);
            this.Name = "Form1";
            this.Text = "Form1";
            ((System.ComponentModel.ISupportInitialize)(this.image_Box)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.PictureBox image_Box;
        private System.Windows.Forms.Button btn_opem;
        private System.Windows.Forms.OpenFileDialog openFileDialog;
    }
}

