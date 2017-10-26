namespace Steuerung
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.PB_Bildschirm = new System.Windows.Forms.PictureBox();
            this.lbl_temp = new System.Windows.Forms.Label();
            this.Abfrage = new System.Windows.Forms.Timer(this.components);
            this.lbl_ping = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.PB_Bildschirm)).BeginInit();
            this.SuspendLayout();
            // 
            // PB_Bildschirm
            // 
            this.PB_Bildschirm.BackColor = System.Drawing.SystemColors.ActiveCaption;
            this.PB_Bildschirm.Location = new System.Drawing.Point(12, 12);
            this.PB_Bildschirm.Name = "PB_Bildschirm";
            this.PB_Bildschirm.Size = new System.Drawing.Size(656, 499);
            this.PB_Bildschirm.TabIndex = 0;
            this.PB_Bildschirm.TabStop = false;
            // 
            // lbl_temp
            // 
            this.lbl_temp.AutoSize = true;
            this.lbl_temp.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbl_temp.Location = new System.Drawing.Point(771, 12);
            this.lbl_temp.Name = "lbl_temp";
            this.lbl_temp.Size = new System.Drawing.Size(129, 18);
            this.lbl_temp.TabIndex = 1;
            this.lbl_temp.Text = "Temperatur : 30°C";
            // 
            // Abfrage
            // 
            this.Abfrage.Interval = 500;
            this.Abfrage.Tick += new System.EventHandler(this.Abfrage_Tick);
            // 
            // lbl_ping
            // 
            this.lbl_ping.AutoSize = true;
            this.lbl_ping.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbl_ping.Location = new System.Drawing.Point(810, 30);
            this.lbl_ping.Name = "lbl_ping";
            this.lbl_ping.Size = new System.Drawing.Size(90, 18);
            this.lbl_ping.TabIndex = 2;
            this.lbl_ping.Text = "Ping: 100ms";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(912, 621);
            this.Controls.Add(this.lbl_ping);
            this.Controls.Add(this.lbl_temp);
            this.Controls.Add(this.PB_Bildschirm);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Form1";
            this.Text = "Steuerung";
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.PB_Bildschirm)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.PictureBox PB_Bildschirm;
        private System.Windows.Forms.Label lbl_temp;
        private System.Windows.Forms.Timer Abfrage;
        private System.Windows.Forms.Label lbl_ping;
    }
}

